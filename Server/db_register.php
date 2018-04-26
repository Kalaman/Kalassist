<?php
  session_start();
  require "db_connect.php";

  $username = $_POST['username'];
  $password = $_POST['password'];
  $email = $_POST['email'];

  $password = md5($password);

  $register_query = "INSERT INTO User VALUES ('$username','$email','$password')";

  $query_result = mysqli_query($connection,$register_query);

  if (mysqli_affected_rows($connection) == 1) {
    echo "REGISTER OK";

    $_SESSION['username'] = $username;
    $_SESSION['login'] = "OK";
  }
  else if (mysqli_errno($connection) == 1062)
  {
    echo "USER ALREADY EXISTS";
  }
  else {
    echo "REGISTER ERROR";
  }

?>
