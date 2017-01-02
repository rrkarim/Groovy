<?php
for($i = 0; $i < 10000000; $i++) {

}
require_once 'include/search_class.php';
require_once 'include/news.php';

$query = "";
$mask = "";
$id = -1;

$searchObject = new SearchC();
$newsObject = new News();

if(isset($_POST['query'])){
	$query = $_POST['query'];
}
if(isset($_POST['mask'])){
    $mask = $_POST['mask'];
}

if(isset($_POST['id'])){
    $id = $_POST['id'];
}

$json = $searchObject->search($query, $mask, $id);
echo $json;

?>