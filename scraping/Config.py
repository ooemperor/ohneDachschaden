"""
Config loader for database access.
Author: Michael Kaiser
"""
import json
import psycopg2


class Config:
    """
    Class that holds config params for all the scrapers
    """

    def __init__(self):
        """
        Constructor for the config class
        """
        self.path = "../config.sec.json"

        f = open("../config.sec.json")
        raw = f.read()
        f.close()

        raw = json.loads(raw)

        self.host = raw['host']
        self.password = raw['password']
        self.username = raw['username']
        self.port = raw['port']
        self.database = raw['database']

    def get_connection(self):
        """
        Generates and returns a database connection
        :return: connection
        """

        conn = psycopg2.connect(
            dbname=self.database,
            user=self.username,
            password=self.password,
            host=self.host,
            port=self.port
        )

        return conn


config = Config()
