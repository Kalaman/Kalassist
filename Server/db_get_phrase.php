<?php
  session_start();
  require "db_connect.php";

  $question = $_POST['question'];

  $register_query = "SELECT * FROM TeachedPhrases WHERE question LIKE '%$question%' ORDER BY RAND() LIMIT 1";
  $query_result = mysqli_query($connection,$register_query) or die (mysqli_error($connection));

  $response = mysqli_fetch_assoc($query_result);
  $jsonResponse = array("Creator" => $response['Creator'],
                        "Question" => $response['Question'],
                        "Answer" => $response['Answer'],
                        "TPID" => $response['TPID'],
                      );
  echo $jsonResponse['Answer'] != null ? json_encode($jsonResponse) : "NO_RESULT";

?>
