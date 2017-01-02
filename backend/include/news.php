<?php
	include_once 'db.php';

	class News {
		
		private $db;
		private $db_table = "news";
		
		public function __construct(){
			$this->db = new DbConnect();
		}
		
		public function getNews($user_id, $type_id, $id) {
			$sample = $this->db_table;
			$query = "";
			if($user_id < 0 && $type_id >= 0) {
				$query = "select * from `$sample` where type_id = '$type_id' ORDER BY date DESC";
			}
			else if($type_id < 0 && $user_id >= 0) {
				$query = "select * from `$sample` where author_id = '$user_id'  ORDER BY date DESC";
			}
			else if($type_id < 0 && $user_id < 0) {
					
				$query = "select * from `$sample` ORDER BY date DESC";
			}
			else {
				$query = "select * from `$sample` where author_id = '$user_id' AND type_id = '$type_id' ORDER BY date DESC";
			}
			$result = mysqli_query($this->db->getDb(), $query);

			$rows = array();
			$len = 0;
			while($r = mysqli_fetch_assoc($result)) {

				// get info about each user
			    $user_id = 1; // currently // super user
			    $author_id = $r['author_id'];
			    	
			    $query_user = "select id, name, surname, image_small, date from users where id = '$author_id'";
			    $result_for_user = mysqli_query($this->db->getDb(), $query_user);
			    
			    while($s = mysqli_fetch_assoc($result_for_user)) {
			    	$r['user'] = $s;
			    }
			    //

			    // get info about each post action // likes
			    $post_id = $r['id'];
			    $query_action_like = "select id from likes where uid = '$id' AND pid = '$post_id '";
			    $result_for_likes = mysqli_query($this->db->getDb(), $query_action_like);
			    if(mysqli_num_rows($result_for_likes) > 0) {
			    	$r['actions']['like'] = 1;
			    }
				else {
					$r['actions']['like'] = 0;
				}
			    //

			    // get info about each post action // repost

				$query_action_post = "select id from repost where uid = '$id' AND pid = '$post_id '";
			    $result_for_post = mysqli_query($this->db->getDb(), $query_action_post);
			    if(mysqli_num_rows($result_for_post) > 0) {
			    	$r['actions']['post'] = 1;
			    }
				else {
					$r['actions']['post'] = 0;
				}

			    //

			    $rows[] = $r;
			    $len += 1;
			}
			$rows['success'] = $len;
			mysqli_close($this->db->getDb());
			return json_encode($rows);
		}

		public function getFeed($id) {

			$query = "select upid from followers where uid = '$id'";

			$result = mysqli_query($this->db->getDb(), $query);

			$folls = array();
			
			while($f = mysqli_fetch_assoc($result)) {
			    $folls[] = $f['upid'];   
			}

			$sample = $this->db_table;
			
			$ids = join("','",$folls); 

			$query = "select * from `$sample` where author_id IN ('$ids') ORDER BY date DESC";

			$result = mysqli_query($this->db->getDb(), $query);

			$rows = array();
			$len = 0;
			while($r = mysqli_fetch_assoc($result)) {

				// get info about each user
			    $user_id = 1; // currently // super user
			    $author_id = $r['author_id'];
			    	
			    $query_user = "select id, name, surname, image_small, date from users where id = '$author_id'";
			    $result_for_user = mysqli_query($this->db->getDb(), $query_user);
			    
			    while($s = mysqli_fetch_assoc($result_for_user)) {
			    	$r['user'] = $s;
			    }
			    //

			    // get info about each post action // likes
			    $post_id = $r['id'];
			    $query_action_like = "select id from likes where uid = '$id' AND pid = '$post_id '";
			    $result_for_likes = mysqli_query($this->db->getDb(), $query_action_like);
			    if(mysqli_num_rows($result_for_likes) > 0) {
			    	$r['actions']['like'] = 1;
			    }
				else {
					$r['actions']['like'] = 0;
				}
			    //

			    // get info about each post action // repost

				$query_action_post = "select id from repost where uid = '$id' AND pid = '$post_id '";
			    $result_for_post = mysqli_query($this->db->getDb(), $query_action_post);
			    if(mysqli_num_rows($result_for_post) > 0) {
			    	$r['actions']['post'] = 1;
			    }
				else {
					$r['actions']['post'] = 0;
				}

			    //

			    $rows[] = $r;
			    $len += 1;
			}
			$rows['success'] = $len;
			mysqli_close($this->db->getDb());
			return json_encode($rows);

		}

		public function createNewPost($title, $header_image, $text, $type_id, $country_id){

			$query = "INSERT INTO news (title, header_image, text, type_id, country_id) values ('$title', '$header_image', '$text', '$type_id', '$country_id')";
			$inserted = mysqli_query($this->db->getDb(), $query);
			
			if($inserted == 1){
				$json['success'] = 1;									
			} else {
				$json['success'] = 0;
			}
			mysqli_close($this->db->getDb());
			
			return $json;
		}


		public function likePost($uid, $id) {
			$query = "UPDATE news SET likes_count = likes_count + 1 WHERE id = '$id'";
			$increased = mysqli_query($this->db->getDb(), $query);

			$queryUser = "INSERT into likes (uid, pid) values ('$uid', '$id')";
			$inserted = mysqli_query($this->db->getDb(), $queryUser);


			if($increased == 1) {
				if($inserted == 1) {
					$json['success'] = 1;
				}
				else {
					$json['success'] = 0;
					$query = "UPDATE news SET likes_count = likes_count - 1 WHERE id = '$id'";
					$increased = mysqli_query($this->db->getDb(), $query);
				}
			}
			else {
				$json['success'] = 0;
			}

			mysqli_close($this->db->getDb());
			return json_encode($json);
		}

		public function postPost($uid, $id) {
			$query = "UPDATE news SET rep_counts = rep_counts + 1 WHERE id = '$id'";
			$increased = mysqli_query($this->db->getDb(), $query);

			$queryUser = "INSERT into repost (uid, pid) values ('$uid', '$id')";
			$inserted = mysqli_query($this->db->getDb(), $queryUser);


			if($increased == 1) {
				if($inserted == 1) {
					$json['success'] = 1;
				}
				else {
					$json['success'] = 0;
					$query = "UPDATE news SET rep_counts = rep_counts - 1 WHERE id = '$id'";
					$increased = mysqli_query($this->db->getDb(), $query);
				}
			}
			else {
				$json['success'] = 0;
			}

			mysqli_close($this->db->getDb());
			return json_encode($json);
		}
	}

?>