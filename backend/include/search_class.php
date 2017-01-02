<?php
	include_once 'db.php';

	class SearchC {
		
		private $db;
		private $db_table = "news";
		
		public function __construct(){
			$this->db = new DbConnect();
		}

		public function search($q, $mask, $id) {
			$dsa = $q;
			
				
			if($mask > 0) {
				if(empty($q)) {
					$query = "select * from news where (type_id & '$mask') > 0";
				}
				else {
					$query = "select * from news where LOWER(title) like LOWER('%$q%') AND (type_id & '$mask') > 0";
				}
				
			}
			else {
				if(empty($q)) {
					$query = "select * from news";
				}
				else {
					$query = "select * from news where LOWER(title) like LOWER('%$q%')";
				}
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
		
	}

?>