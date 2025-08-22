# Setup script for use on debian
echo '### setup basic linux requirements (apt) ###'
apt update
apt-get install python3 python3-venv unzip curl postgresql-client libpq-dev -y --fix-missing
