<?php
	require_once 'include/country.php';
	$id = "";
	if(isset($_POST['id'])){
		$id = $_POST['id'];
	}
	$countryObject = new Country();
	if(empty($id)){
		$json = $countryObject->getAllCountries();
		echo json_encode($json);
	}
	else {
		$json = $countryObject->getCountry($id);
		echo json_encode($json);
	}
?>