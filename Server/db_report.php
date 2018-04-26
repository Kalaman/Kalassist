<?php
  session_start();
  require "db_connect.php";

  $username = $_POST['username'];
  $phraseid= $_POST['tpid'];

  $query= "INSERT INTO PhraseReport VALUES ($phraseid,'$username')";

  $query_result = mysqli_query($connection,$query);

  if (mysqli_affected_rows($connection) == 1) {
    echo "REPORTED";
  }
  else if (mysqli_errno($connection) == 1062) {
    echo "USER ALREADY REPORTED";
  }
  else {
    echo "REPORT ERROR";
  }

?>
