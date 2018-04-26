<?php
  session_start();
  require "db_connect.php";

  $question = $_POST['question'];
  $answer = $_POST['answer'];
  $username = $_POST['username'];

  $register_query = "INSERT INTO TeachedPhrases VALUES ('','$username','$question','$answer')";

  $query_result = mysqli_query($connection,$register_query) or die (mysqli_error($connection));

  if (mysqli_affected_rows($connection) == 1) {
    echo "PHRASE LEARNED";
  }
  else {
    echo "PHRASE NOT LEARNED";
  }

?>
