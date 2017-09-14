<?php
$conn = mysqli_connect("asapkota.spinetail.cdu.edu.au","asapkota_aayush","Engineering47","asapkota_stealth_patrolling");

$name = $_POST["name"];
$organization = $_POST["organization"];
$username = $_POST["username"];
$password = $_POST["password"];
$userType = $_POST["user_type"];


$statement = mysqli_prepare($conn, "INSERT INTO user (name, organization,username,password, user_type) VALUES (?, ?, ?, ?, ?)");
mysqli_stmt_bind_param($statement, "sssss",$name ,$organization, $username, $password, $userType);
mysqli_stmt_execute($statement);

$response = array();
$response["sucess"] = true;

echo json_encode($response);
?>
