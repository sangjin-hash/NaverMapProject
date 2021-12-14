<?php
header("application/json");

require_once 'con.php';

$sql = mysqli_query($con, "SELECT * FROM seat");

$response = array();
$preRow="";
$ppreRow="";
$cnt=0;
$preSection="";

while($row = mysqli_fetch_assoc($sql)){


    if($preSection != $row[sectionNo])
	{$cnt = 0;}
    if($cnt >= 3)
    {	
    	array_push($response, array(
        	'seatNo' => $ppreRow,
        	'left' => left($ppreRow),
		'top' => top($ppreRow)
    	));
    }
    $preSection = $row[sectionNo];

    if($row[isEmpty] == 1)
    {
	$cnt = $cnt + 1;
    }
    else
    {
	$cnt = 0;
    }
	$ppreRow = $preRow;
	$preRow = $row[seatNo];
}

function left($num){
	if(0<=$num && $num <=14){
		return 17 + ($num * 23);
	}else if(15<=$num && $num <=38){
		return 409 + ($num - 15) * 23;
	}else if(39<=$num && $num <=50){
		return 63 + ($num - 39) * 23;
	}else if(51<=$num && $num <=69){
		return 432 + ($num - 51) * 23;
	}else if(70<=$num && $num <=81){
		return 63 + ($num - 70) * 23;
	}else if(82<=$num && $num <=100){
		return 432 + ($num - 82) * 23;
	}else if(101<=$num && $num <=105){
		return 942 + ($num - 101) * 23;
	}else if(106<=$num && $num <=110){
		return 40 + ($num - 106) * 23;
	}else if(111<=$num && $num <=115){
		return 224 + ($num - 111) * 23;
	}else if(116<=$num && $num <=134){
		return 432 + ($num - 116) * 23;
	}else if(135<=$num && $num <=137){
		return 942 + ($num - 135) * 23;
	}else if(138<=$num && $num <=142){
		return 224 + ($num - 138) * 23;
	}else if(143<=$num && $num <=161){
		return 432 + ($num - 143) * 23;
	}else if(162<=$num && $num <=179){
		return 432 + ($num - 162) * 23;
	}
}

function top($num){
	if(0<=$num && $num <=38){
		return 150;
	}else if(39<=$num && $num <=69){
		return 315;
	}else if(70<=$num && $num <=105){
		return 495;
	}else if(106<=$num && $num <=137){
		return 660;
	}else if(138<=$num && $num <=161){
		return 840;
	}else if(162<=$num && $num <=179){
		return 1010;
	}
}


echo json_encode($response);
?>
