<?php

class DB {

    function __construct() {
        $host = 'localhost';
        $user = 'root';
        $password = '';
        $db = 'findfriends_db';
        $this->conn = new mysqli($host, $user, $password, $db);
        if ($this->conn->connect_errno) {
            printf("Не удалось подключиться: %s\n", $this->conn->connect_error);
            exit();
        }
    }

    function __destruct() {
        $this->conn->close();
    }

    //USER

    public function insertUser($login, $hash , $salt){
		$query = 'INSERT INTO users (login, password , salt) VALUES ("'.$login . '" , "' . $hash . '" , "' . $salt .'")';
        return $this->conn->query($query);
    }

    public function login($login, $token){
        $query = 'UPDATE users SET token="'.$token.'" WHERE login="'.$login.'"';
        $this->conn->query($query);
        return true;
    }

    public function logout($login){
        $query = 'UPDATE users SET token="" WHERE login="'.$login.'"';
        $this->conn->query($query);
        return true;
    }
    
    public function getUserByLogin($login) {
        $query = 'SELECT * FROM users WHERE login="' . $login . '"';
        $result = $this->conn->query($query);
        $res = array();
        while ($row = $result->fetch_object()) {
            $res = $row;
        }
        return $res;
    }

    public function getUserByToken($token) {
        $query = 'SELECT * FROM users WHERE token="' . $token . '"';
        $result = $this->conn->query($query);
        $res = array();
        while ($row = $result->fetch_object()) {
            $res = $row;
        }
        return $res;
    }

    public function enterRoom($userId, $roomId){
        $query = 'UPDATE users SET room_id="'.$roomId.'" WHERE id="'.$userId.'"';
        $this->conn->query($query);
        return true;
    }
    
    public function leaveRoom($id){
        $query = 'UPDATE users SET room_id="" WHERE id="'.$id.'"';
        $this->conn->query($query);
        return true;
    }

    public function saveLocation($id, $latitude, $longitude){
        $query = 'UPDATE users SET latitude="'.$latitude.'", longitude="'.$longitude.'" WHERE id="'.$id.'"';
        $this->conn->query($query);
        return true;
    }

    public function retrieveRoomUsers($roomId){
        $query = 'SELECT login, avatar, latitude, longitude FROM users WHERE room_id="'.$roomId.'"';
        $result = $this->conn->query($query);
        $res = array();
        while ($row = $result->fetch_object()) {
            $res[] = $row;
        }
        return $res;
    }

    public function updateUserAvatar($id, $avatar){
        $query = 'UPDATE users SET avatar="'.$avatar.'" WHERE id="'.$id.'"';
        $this->conn->query($query);
        return true;
    }


    //ROOM

    public function createRoom($id, $roomName) {
        $query = 'INSERT INTO rooms (name, adminId) VALUES ("'.$roomName . '" , "' . $id . '")';
        $this->conn->query($query);
        return $this->conn->insert_id;
    }
    
    public function setRoomLink($id, $link){
        $query = 'UPDATE rooms SET link="' .$link. '" WHERE id="'.$id.'"';
        $this->conn->query($query);
        return true;
    }


    public function getRoomByLink($link) {
        $query = 'SELECT * FROM rooms WHERE link="' . $link . '"';
        $result = $this->conn->query($query);
        $res = array();
        while ($row = $result->fetch_object()) {
            $res = $row;
        }
        return $res;
    }

    public function getRoomByAdminId($id) {
        $query = 'SELECT * FROM rooms WHERE adminId="' . $id . '"';
        $result = $this->conn->query($query);
        if ($result) {
            $res = array();
            while ($row = $result->fetch_object()) {
                $res = $row;
            }
            return $res;
        }
        return false;
    }

    public function getUsersByRoomId($id) {
        $query = 'SELECT * FROM users WHERE room_id="' . $id . '"';
        $result = $this->conn->query($query);
        $res = array();
        while ($row = $result->fetch_object()) {
            $res[] = $row;
        }
        return $res;
    }

    public function getRoomById($id) {
        $query = 'SELECT * FROM rooms WHERE id="' . $id . '"';
        $result = $this->conn->query($query);
        $res = array();
        while ($row = $result->fetch_object()) {
            $res[] = $row;
        }
        return $res;
    }

    public function deleteRoomByAdminId($adminId) {
        $query = 'DELETE FROM rooms WHERE adminId="' . $adminId . '"';
        $this->conn->query($query);
        return true;
    }

    public function putUserIntoRoom($userId, $roomId){
        $query = 'UPDATE users SET room_id="'.$roomId.'" WHERE id="'.$userId.'"';
        $this->conn->query($query);
        return true;
    }

}