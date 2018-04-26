<?php
  session_start();
  require "db_connect.php";

  $question = $_POST['question'];
  $answer = $_POST['answer'];
  $command_type = $_POST['command_type'];
  $username = $_POST['username'];

  $password = md5($password);

  $register_query = "INSERT INTO Queries VALUES ('','$username','$question','$answer','$command_type')";

  $query_result = mysqli_query($connection,$register_query) or die (mysqli_error($connection));

  if (mysqli_affected_rows($connection) == 1) {
    echo "QUERIE SAVED";
  }
  else {
    echo "QUERIE SAVE ERROR";
  }

?>
