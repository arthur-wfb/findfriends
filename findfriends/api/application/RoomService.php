<?php

class RoomService {

    function __construct($db) {
        $this->db = $db;
    }

    public function createRoom($token, $roomName){
        if ($token && $roomName) {
            $user = $this->db->getUserByToken($token);
            if ($user) {
                $roomId = $this->db->createRoom($user->id, $roomName);
                $this->db->putUserIntoRoom($user->id, $roomId);
                $link = md5(strval($roomId));
                $this->db->setRoomLink($roomId, $link);
                return array('code' => Constants::SUCCESS, 'data' => $link);
            }
            return array('code' => Constants::NOT_AUTHORIZED);

        }
        return false;
    }

    public function deleteRoom($token){
        if ($token) {
            $user = $this->db->getUserByToken($token);
            if ($user) {
                $roomId = $this->db->deleteRoomByAdminId($user->id);
                return array('code' => Constants::SUCCESS);
            }
        }
        return array('code' => Constants::NOT_AUTHORIZED);
    }

    public function getRoomById($id) {
        return $this->db->getRoomById($id);
    }
}