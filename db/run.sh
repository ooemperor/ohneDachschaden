# Basic run script for the load
echo '### you are about to load all the data ###'
cd import/download || exit
./download_admin_data.sh
cd ../../import/sh || exit
./load_inb_admin_data.sh
cd ./import/sh || exit
./load_inb_gvb_data.sh