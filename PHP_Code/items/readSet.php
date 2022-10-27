<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once '../config/Database.php';
include_once '../class/Items.php';

$database = new Database();
$db = $database->getConnection();
 
$items = new Items($db);

// $items->id = (isset($_GET['id']) && $_GET['id']) ? $_GET['id'] : '0';
$dataSetIndex = (isset($_GET['set']) && $_GET['set']) ? $_GET['set'] : '0';

$result = $items->readSet($dataSetIndex);

if($result->num_rows > 0){    
    $itemRecords=array();
    $itemRecords["status"]="200";
    $itemRecords["items"]=array(); 
	while ($item = $result->fetch_assoc()) { 	
        extract($item); 
        $itemDetails=array(
            "id" => $id,
            "name" => $name,
            "description" => $description,
			"price" => $price,
            "category_id" => $category_id,            
			"created" => $created,
            "modified" => $modified			
        ); 
       array_push($itemRecords["items"], $itemDetails);
    }    
    http_response_code(200);     
    echo json_encode($itemRecords);
}else{     
    http_response_code(404);     
    echo json_encode(
        array(
            "status" => "404",
            "message" => "No item found."
            )
    );
} 