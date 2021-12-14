<?php
header("application/json");

require_once'con.php';

$sql = mysqli_query($con, "SELECT * FROM seat");

$response = array();

while($row = mysqli_fetch_assoc($sql))
{
    array_push($response, array(
	'seatNo' => $row[seatNo],
	'isEmpty' => $row[isEmpty]
    ));
}

echo json_encode($response);
?>
