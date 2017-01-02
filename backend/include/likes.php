<?php
	include_once 'db.php';

	class Likes {
		
		private $db;
		private $db_table = "likes";
		
		public function __construct(){
			$this->db = new DbConnect();
		}
		public function add($uid, $pid) {
			$query = "INSERT INTO likes (uid, pid) values ('$uid', '$pid')";
			$inserted = mysqli_query($this->db->getDb(), $query);
			if($inserted == 1){
				$json['success'] = 1;									
			}else{
				$json['success'] = 0;
			}
			mysqli_close($this->db->getDb());
			
			return json_encode($json);
		}

		public function getLikes($uid) {
			$query = "select pid from likes where uid = '$uid'";
			$result = mysqli_query($this->db->getDb(), $query);

			$rows = array();
			$len = 0;
			while($r = mysqli_fetch_assoc($result)) {
			    $rows[] = $r;
			    $len += 1;
			}
			$rows['success'] = $len;
			mysqli_close($this->db->getDb());

			return json_encode($rows);
		}
	}

?>