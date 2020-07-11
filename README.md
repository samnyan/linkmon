# Linkmon

A simple network status logging server.

Simply post the payload with curl, the server will save it.

## Why

My router has multiple wan connection. I just want to log the latency and uptime and not use too much resource on my router. So I decided to use crontab with a bash script that perform a ping test and curl the result to my NAS.

## Showcase

### LinkMon Web Interface

![LinkMon_Web](https://i.loli.net/2020/07/12/uQ1PHg7GBVkocJe.png)

### Simple Desktop Client

Check the [client](https://github.com/samnyan/linkmon/tree/client) branch.

![LinkMon_Client](https://i.loli.net/2020/07/12/f75mc2wslIuzEF4.png)
