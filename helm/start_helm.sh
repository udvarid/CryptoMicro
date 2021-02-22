echo delete previous version
helm delete crypto --namespace crypto
kubectl delete namespace crypto

echo start new version
kubectl create namespace crypto
helm install crypto . --namespace crypto
