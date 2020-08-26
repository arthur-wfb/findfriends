<?php
require_once('db/DB.php');
require_once('application/UserService.php');
require_once('application/RoomService.php');

class Api {

    function __construct() {
        $db = new DB();
        $this->userService = new UserService($db);
        $this->roomService = new RoomService($db);
    }

    public function register($params) {
        return $this->userService->register($params['login'], $params['password']);
    }

    public function login($params) {
        return $this->userService->login($params['login'], $params['password']);
    }

    public function logout($params) {
        return $this->userService->logout($params['login'], $params['token']);
    }

    public function createRoom($params) {
        return $this->roomService->createRoom($params['token'], $params['room_name']);
    }

    public function deleteRoom($params) {
        return $this->roomService->deleteRoom($params['token']);
    }

    public function enterRoom($params) {
        return $this->userService->enterRoom($params['token'], $params['link']);
    }

    public function leaveRoom($params) {
        return $this->userService->leaveRoom($params['token']);
    }

    public function saveLocation($params) {
        return $this->userService->saveLocation($params['token'], $params['latitude'], $params['longitude']);
    }

    public function retrieveRoomUsers($params) {
        return $this->userService->retrieveRoomUsers($params['token']);
    }

    public function retrieveUsersRoomInfo($params) {
        return $this->userService->retrieveUsersRoomInfo($params['token']);
    }

    public function updateUserAvatar($params) {
        return $this->userService->updateUserAvatar($params['token'], intval($params['avatar']));
    }

    public function putUserIntoRoom($params) {
        return $this->userService->putUserIntoRoom($params['token'], $params['link']);
    }

    public function getUsersByRoomId($id) {
        return $this->userService->getUsersByRoomId($id);
    }

    public function getRoomById($id) {
        return $this->roomService->getRoomById($id);
    }
}