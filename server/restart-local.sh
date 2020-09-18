docker-compose up -d
containerId=`docker container ls | grep t-boss-back | cut -d ' ' -f 1`;
echo ${containerId}
docker cp script.sh ${containerId}:/app/script.sh
docker exec -it ${containerId} /bin/bash -c "sh /app/script.sh"
echo "first stage finished"

docker cp ${containerId}:/root/.ssh/id_rsa.pub ./docker_id_rsa.pub
data=$(cat ./docker_id_rsa.pub)
SF=`sed -n -e "/temp-begin/="  ~/.ssh/authorized_keys`
echo ${SF}
TF=`sed -n -e "/temp-end/="  ~/.ssh/authorized_keys`
echo ${TF}
cd ~/.ssh
#pwd
#sed -i "" '${SF},${TF}d' authorized_keys
echo -e "\n#temp-begin\n" >> ~/.ssh/authorized_keys
echo ${data} >>  ~/.ssh/authorized_keys
echo -e "\n#temp-end\n" >>  ~/.ssh/authorized_keys
echo "second stage finished"

cd -
rm docker_id_rsa.pub
docker exec -it ${containerId} /bin/bash -c "pwd&&ssh weiqun@192.168.199.126 -o stricthostkeychecking=no 'pwd;exit' "
docker restart ${containerId}
