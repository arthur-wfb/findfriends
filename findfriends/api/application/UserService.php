<?php
require_once('api/Constants.php');

class UserService {

    function __construct($db) {
        $this->db = $db;
    }

    public function register($login, $password){
        if ($login && $password) {
            if ($this->db->getUserByLogin($login)) {
                return array('code' => Constants::LOGIN_EXISTS);
            }
            $salt = '';
            $saltLength = 8;
            for($i=0; $i<$saltLength; $i++) {
                $salt .= chr(mt_rand(33,126));
            }
            $hash = md5($password.$salt);
            $this->db->insertUser($login, $hash, $salt);
            return array('code' => Constants::SUCCESS);
        }
        return false;
    }

    public function login($login, $password){
        if ($login && $password) {
            $user = $this->db->getUserByLogin($login);
            if (!$user) {
                return array('code' => Constants::NOT_AUTHORIZED);
            }
            $hash = md5($password.$user->salt);
            if ($hash == $user->password) {
                $token = md5($hash.strval(rand()));
                $this->db->login($login, $token);
                return array('code' => Constants::SUCCESS, 'data' => array('token' => $token, 'avatar' => $user->avatar));
            }
            return false;
        }
        return false;
    }

    public function logout($login, $token){
        if ($login && $token) {
            $user = $this->db->getUserByLogin($login);
            if (!$user) {
                return array('code' => Constants::NOT_AUTHORIZED);
            }
            if ($token == $user->token) {
                $this->db->logout($login);
                return array('code' => Constants::SUCCESS);
            }
            return false;
        }
        return false;
    }

    public function enterRoom($token, $link){
        if ($token && $link) {
            $user = $this->db->getUserByToken($token);
            if ($user != null) {
                $roomId = $this->db->getRoomByLink($link)->id;
                $this->db->enterRoom($user->id, $roomId);
                return array('code' => Constants::SUCCESS);
            }
        }
        return false;
    }

    public function leaveRoom($token){
        if ($token) {
            $user = $this->db->getUserByToken($token);
            if ($user != null) {
                $this->db->leaveRoom($user->id);
                return array('code' => Constants::SUCCESS);
            }
        }
        return array('code' => Constants::NOT_AUTHORIZED);
    }

    public function retrieveUsersRoomInfo($token){
        if ($token) {
            $user = $this->db->getUserByToken($token);
            if (!$user) {
                return array('code' => Constants::NOT_AUTHORIZED);
            }
            $room = $this->db->getRoomByAdminId($user->id);
            if ($room) {
                return array('code' => Constants::SUCCESS, 'data' => array('roomName' => $room->name, 'roomLink' => $room->link));
            }
            return array('code' => Constants::ROOM_NOT_EXISTS);
        }
        return false;
    }

    public function putUserIntoRoom($token, $link) {
        $user = $this->db->getUserByToken($token);
        $room = $this->db->getRoomByLink($link);
        if (!$user || !$link) {
            return false;
        }
        $this->db->putUserIntoRoom($user->id, $room->id);
        return array('result' => 'ok');
    }

    public function saveLocation($token, $latitude, $longitude) {
        $user = $this->db->getUserByToken($token);
        if ($user) {
            $this->db->saveLocation($user->id, $latitude, $longitude);           
            return array('code' => Constants::SUCCESS);
        }
        return array('code' => Constants::NOT_AUTHORIZED);
    }

    public function retrieveRoomUsers($token) {
        $user = $this->db->getUserByToken($token);
        if ($user) {
            $usersInRoom = $this->db->retrieveRoomUsers($user->room_id);           
            return array('code' => Constants::SUCCESS, 'data' => $usersInRoom);
        }
        return array('code' => Constants::NOT_AUTHORIZED);
    }

    public function updateUserAvatar($token, $avatar) {
        $user = $this->db->getUserByToken($token);
        if ($user) {
            $this->db->updateUserAvatar($user->id, $avatar);           
            return array('code' => Constants::SUCCESS);
        }
        return array('code' => Constants::NOT_AUTHORIZED);
    }
    
    public function getUsersByRoomId($id) {
        return $this->db->getUsersByRoomId($id);
    }
    
}