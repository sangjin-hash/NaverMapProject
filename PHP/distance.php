<?php

header("applicatopm/json");

require_once 'con.php';

$sql = mysqli_query($con, "SELECT * FROM seat where priority =(SELECT min(priority) from seat)");

$response = array();

while($row = mysqli_fetch_assoc($sql))
{
    array_push($response, array(
	'seatNo' => $row[seatNo]
    ));
}
echo json_encode($response);
