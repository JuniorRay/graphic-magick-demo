#!/bin/sh
#<!--Create By Junior 2020/11/25 -->
service='/etc/systemd/system/tcl-file-server.service'

systemctl disable tcl-file-server.service

rm -rf $service
