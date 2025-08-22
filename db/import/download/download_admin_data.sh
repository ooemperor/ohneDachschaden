# Download and dezip all the files for the admin data. 
cd ../..
mkdir -p data
cd data

rm -r ch

curl -J -O "https://public.madd.bfs.admin.ch/ch.zip"
unzip -u ch.zip -d ./ch

#remove the old zip file
rm -r ch.zip

echo '####     PREPROCESSING     ####'
echo 'apartment' && sed -i 's/"//g' ./ch/wohnung_logement_abitazione.csv
echo 'building' && sed -i 's/"//g' ./ch/gebaeude_batiment_edificio.csv
echo 'entry' && sed -i 's/"//g' ./ch/eingang_entree_entrata.csv
echo 'codes' && sed -i 's/"//g' ./ch/kodes_codes_codici.csv
echo '####  PREPROCESSING  DONE ####'