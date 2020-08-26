<?php
require_once('db/DB.php');
require_once('application/UserService.php');
require_once('application/RoomService.php');

class Constants {
    const OK = "OK";
    const ERROR = "ERROR";
    const SUCCESS = "SUCCESS";
    const LOGIN_EXISTS = "LOGIN_EXISTS";
    const NOT_AUTHORIZED = "NOT_AUTHORIZED";
    const ROOM_NOT_EXISTS = "ROOM_NOT_EXISTS";
}