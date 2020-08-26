<?php
error_reporting(-1);
header('Access-Control-Allow-Origin: *');
require_once('api/Api.php');

function router($params) {
    $method = $params['method'];
    if ($method) {
        $api = new Api();
        switch ($method) {
            case 'register': return $api->register($params);
            case 'login': return $api->login($params);
            case 'logout': return $api->logout($params);
            case 'create_room': return $api->createRoom($params);
            case 'delete_room': return $api->deleteRoom($params);
            case 'enter_room': return $api->enterRoom($params);
            case 'leave_room': return $api->leaveRoom($params);
            case 'save_location': return $api->saveLocation($params);
            case 'retrieve_room_users': return $api->retrieveRoomUsers($params);
            case 'retrieve_users_room_info': return $api->retrieveUsersRoomInfo($params);
            case 'update_avatar': return $api->updateUserAvatar($params);
            case 'put_user_into_room': return $api->putUserIntoRoom($params);
            default: return false;
        }
    }
    return false;
}

function answer($answer) {
    if ($answer['code']) {
        if (isset($answer['data'])) {
            return array(
                'result' => $answer['code'],
                'data' => $answer['data']
            );
        }
        return array('result' => $answer['code']);
    }
    return array(
        'result' => Constants::ERROR,
        'error' => array(
            'code' => 9000,
            'text' => 'unknown error'
        )
    );
}

$method = $_SERVER['REQUEST_METHOD'];
if ($method == 'POST') {
    echo json_encode(answer(router($_REQUEST)));
} elseif ($method == 'GET'){
    echo json_encode(answer(router($_GET)));
}
