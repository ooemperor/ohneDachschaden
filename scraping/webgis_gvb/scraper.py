"""
Scraper for the webgis api that returns dangers for the given EGID
Author: Michael Kaiser
"""

import requests as req
import os.path
from tqdm import tqdm
import sys
from scraping.Config import config


def fetch_egids() -> list:
    """
    Method to fetch all the egids from the database and start querying them.
    Saves the output into files that can then be parsed seperately
    :return: The list of building identifiers
    :rtype: list
    """
    # Connect to your postgres DB
    conn = config.get_connection()

    # Create a cursor object
    cur = conn.cursor()

    # Execute a query
    cur.execute("select egid from public.admin_building where gdekt = 'BE' ORDER BY egid;")

    # Fetch all results as a list of tuples
    results = cur.fetchall()

    print(len(results))

    cur.close()
    conn.close()

    return results


def write_to_file(egid: str, result_json: str) -> None:
    """
    Write the result of the json into the corresponding file
    :param egid: the identifier of the building
    :type egid: str
    :param result_json: The json representing the data
    :type result_json: str
    :return: None
    """
    f = open(f"./results/{egid}.txt", "w")
    f.write(str(result_json))
    f.close()


def fetch_all(egids: list) -> None:
    """
    iterate over all the egids and call the scraper method.
    :param egids: list of building identifiers
    :type egids: list
    :return: None
    """
    for egid in tqdm(egids):
        egid_clean = str(egid)
        egid_clean = egid_clean.replace("('", "")
        egid_clean = egid_clean.replace("',)", "")

        if os.path.exists(f"./results/{egid_clean}.txt"):
            continue
        else:
            res = fetch_single(egid_clean)
            write_to_file(egid_clean, res)


def fetch_single(egid: str) -> dict:
    """
    Fetches the result of the gvb api for a single egid
    :param egid: The identifier of the building to scrape
    :type egid: str
    :return: The retrun value of the api
    :rtype: dicgt
    """

    url = f"https://webgis.gvb.ch/server/rest/services/natur/GEBAEUDE_NATURGEFAHREN_BE_DE_FR/MapServer/1/query?where=GWR_EGID%3D{egid}&text=&objectIds=&time=&timeRelation=esriTimeRelationOverlaps&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&distance=&units=esriSRUnit_Foot&relationParam=&outFields=OBJECTID%2CGWR_EGID%2CBEGID%2COBERFLAECHENABFLUSS%2CHOCHWASSER_SEEN%2CHOCHWASSER_FLIESSGEWAESSER%2CHAGEL%2CSTURM%2CHAUSNUMMER%2CSTRNAME%2CPLZ%2CORTSCHAFT%2CADRESSE%2CADRESSE_POPUP%2COBERFLAECHENABFLUSS_TEXT_DE%2COBERFLAECHENABFLUSS_TEXT_FR%2CFLIESSGEWAESSER_TEXT_DE%2CFLIESSGEWAESSER_TEXT_FR%2CSEEN_TEXT_DE%2CSEEN_TEXT_FR%2CHAGEL_TEXT%2CSTURM_TEXT%2CSHAPE&returnGeometry=true&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=&havingClause=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&historicMoment=&returnDistinctValues=false&resultOffset=&resultRecordCount=&returnExtentOnly=false&sqlFormat=none&datumTransformation=&parameterValues=&rangeValues=&quantizationParameters=&uniqueIds=&returnUniqueIdsOnly=false&featureEncoding=esriDefault&f=pjson"

    result = req.get(url)
    assert result.status_code == 200
    return result.json()


def main():
    """
    Main Runner function to process the data
    :return: None
    """
    results = fetch_egids()
    fetch_all(results)


if __name__ == "__main__":
    sys.exit(main())