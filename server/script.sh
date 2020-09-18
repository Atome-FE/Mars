#!/bin/bash
sed -i 's/#AuthorizedKeysFile/AuthorizedKeysFile/g' /etc/ssh/sshd_config
sed -i 's/#PubkeyAuthentication/PubkeyAuthentication/g' /etc/ssh/sshd_config
mkdir ~/.ssh
cd ~/.ssh
ssh-keygen -f ~/.ssh/id_rsa -t rsa -N ''
mkdir /var/run/sshd
/usr/sbin/sshd

