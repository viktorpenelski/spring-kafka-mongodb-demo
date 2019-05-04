#!/bin/bash

attempts=20
sleep_time=3

if [ -z "$INTEGRATION_TEST_BASE_URL_PORT" ]; then
    app_host_url="127.0.0.1:8080"
else
    app_host_url=$INTEGRATION_TEST_BASE_URL_PORT
fi

function hostResponsive() {
	if curl -f -s $app_host_url > /dev/null ; then
	    echo "true";
	else
	    echo "false";
	fi;
}

while [ $(hostResponsive) == "false" ] && [ $attempts -gt 0 ]
do
	((--attempts))
	sleep $sleep_time
	echo "Failed to reach $app_host_url, retrying for another $((attempts * sleep_time))s"
done

if [[ $atempts -lt 0 ]]; then
	exit 42;
fi

# run the test even if the task is 'up-to-date'
./gradlew --rerun-tasks integrationTest