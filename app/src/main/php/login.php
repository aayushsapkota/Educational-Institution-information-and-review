<?php
$conn = mysqli_connect("asapkota.spinetail.cdu.edu.au","asapkota_aayush","Engineering47","asapkota_stealth_patrolling");

$username = $_POST["username"];
$password = $_POST["password"];

$statement = mysqli_prepare($conn, "SELECT * FROM user WHERE username=? and password=?");
mysqli_stmt_bind_param($statement, "ss", $username, $password);
mysqli_stmt_execute($statement);
mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $userID, $name,$organization,$username,$password,$userType);

$response = array();
$response["sucess"] = false;

while(mysqli_stmt_fetch($statement)){
  $response["sucess"] = true;
  $response["name"] = $name;
  $response["organization"] = $organization;
  $response["username"] = $username;
  $response["password"] = $password;
  $response["user_type"] = $userType;
}

echo json_encode($response);


 ?>
