
FROM consul:1.15.4

EXPOSE 8500/tcp 8600/udp

COPY ./consul-config.json /etc/consul.d/consul-config.json

CMD ["agent","-server", "-bootstrap-expect=1", "-ui","-config-dir=/etc/consul.d"]

# docker run -p 8500:8500 -d consul:1.15.4 agent -server -bootstrap-expect=1