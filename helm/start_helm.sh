if [ $OSTYPE != "msys" ]; then
	echo This is linux, trying to start minikube;
	minikube start;
	minikube addons enable ingress;
fi;

echo delete previous version
helm delete crypto --namespace crypto
kubectl delete namespace crypto

echo start new version
kubectl create namespace crypto
helm install crypto . --namespace crypto
