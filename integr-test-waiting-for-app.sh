function hostResponsive() {
	if curl -f -s localhost:8080 > /dev/null ; then 
	    echo "true"; 
	else 
	    echo "false"; 
	fi;
}

attempts=30
sleep_time=2

while [ $(hostResponsive) == "false" ] && [ $attempts -gt 0 ]
do
	((--attempts))
	sleep $sleep_time
	echo "false with attempts - $attempts"
done

if [[ $atempts -lt 0 ]]; then
	exit 42;
fi

# run the test even if the task is 'up-topdate'
./gradlew --rerun-tasks integrationTest
exit 0;