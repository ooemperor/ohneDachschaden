"""
Processor for the data of the webgis api
Author: Michael Kaiser
"""
import json
from threading import Thread
from time import sleep
import os.path
from tqdm import tqdm
import sys
from scraping.Config import config


def fetch_result(result: str) -> None:
    """
    Read the json out of the result file and write it to the db.
    :param result: the filename which defines the result
    :type result: str
    :return: None
    """
    f = open(f"./results/{result}")
    data = f.read()
    f.close()

    data = data.strip()
    data = data.replace("'", "\"")
    data = data.replace("None", "\"\"")
    try:
        data = json.loads(data)
        features = dict(data)["features"][0]["attributes"]
    except Exception as err:
        return

    insert_in_db(features)


def insert_in_db(features) -> None:
    """
    assembles and executes the insert query into the database
    :param features:
    :return:
    """
    lookup_string = f"SELECT * FROM stage.gvb_dangers WHERE objectid = '{features['OBJECTID']}'"

    conn = config.get_connection()
    # Create a cursor object
    cur = conn.cursor()

    # Execute a query
    cur.execute(lookup_string)
    found = cur.fetchall()

    if len(found) > 0:
        return

    insert_string = f"INSERT INTO stage.gvb_dangers VALUES ('{features['OBJECTID']}', '{features['GWR_EGID']}', '{features['BEGID']}', '{features['OBERFLAECHENABFLUSS']}', '{features['HOCHWASSER_SEEN']}', '{features['HOCHWASSER_FLIESSGEWAESSER']}','{features['HAGEL']}','{features['STURM']}','{features['HAUSNUMMER']}','{features['STRNAME']}',   '{features['PLZ']}', '{features['ORTSCHAFT']}', '{features['ADRESSE']}', '{features['ADRESSE_POPUP']}', '{features['OBERFLAECHENABFLUSS_TEXT_DE']}', '{features['OBERFLAECHENABFLUSS_TEXT_FR']}', '{features['FLIESSGEWAESSER_TEXT_DE']}', '{features['FLIESSGEWAESSER_TEXT_FR']}', '{features['SEEN_TEXT_DE']}', '{features['SEEN_TEXT_FR']}', '{features['HAGEL_TEXT']}', '{features['STURM_TEXT']}')"

    cur.execute(insert_string)
    conn.commit()

    cur.close()
    conn.close()
    return


def main() -> None:
    """
    Main Runner function to process the data
    :return: None
    """
    results = os.listdir("./results")

    for result in tqdm(results):
        # read the json here
        thread = Thread(target=fetch_result, args=(result,))
        thread.start()
        sleep(0.01)


if __name__ == "__main__":
    sys.exit(main())
