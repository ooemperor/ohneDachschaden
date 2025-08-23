"""
Processor for the data of the webgis api
@author: Michael Kaiser
"""
import json

import requests as req
import psycopg2
from time import time, sleep
import os.path
from tqdm import tqdm
import sys


def fetch_result(result: str):
    """
    Read the json out of the result file and write it to the db.
    :param result: the filename
    :return: None
    """
    f = open(f"./results/{result}")
    data = f.read()
    f.close()

    data = data.strip()
    data = data.replace("'", "\"")
    data = data.replace("None", "\"\"")
    data = json.loads(data)
    try:
        features = dict(data)["features"][0]["attributes"]
    except IndexError as err:
        return

    insert_in_db(features)


def insert_in_db(features) -> None:
    """
    assembles and executes the insert query into the database
    :param features:
    :return:
    """
    insert_string = f"INSERT INTO stage.gvb_dangers VALUES ('{features['OBJECTID']}', '{features['GWR_EGID']}', '{features['BEGID']}', '{features['OBERFLAECHENABFLUSS']}', '{features['HOCHWASSER_SEEN']}', '{features['HOCHWASSER_FLIESSGEWAESSER']}','{features['HAGEL']}','{features['STURM']}','{features['HAUSNUMMER']}','{features['STRNAME']}',   '{features['PLZ']}', '{features['ORTSCHAFT']}', '{features['ADRESSE']}', '{features['ADRESSE_POPUP']}', '{features['OBERFLAECHENABFLUSS_TEXT_DE']}', '{features['OBERFLAECHENABFLUSS_TEXT_FR']}', '{features['FLIESSGEWAESSER_TEXT_DE']}', '{features['FLIESSGEWAESSER_TEXT_FR']}', '{features['SEEN_TEXT_DE']}', '{features['SEEN_TEXT_FR']}', '{features['HAGEL_TEXT']}', '{features['STURM_TEXT']}')"
    # Connect to your postgres DB
    conn = psycopg2.connect(
        dbname="UDM",
        user="postgres",
        password="postgres",
        host="localhost",
        port="5432"
    )

    # Create a cursor object
    cur = conn.cursor()

    # Execute a query
    cur.execute(insert_string)
    conn.commit()

    cur.close()
    conn.close()


def main():
    results = os.listdir("./results")

    for result in tqdm(results):
        # read the json here
        fetch_result(result)


if __name__ == "__main__":
    sys.exit(main())
