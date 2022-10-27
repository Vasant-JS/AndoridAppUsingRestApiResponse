<?php
class Items{   
    
    private $itemsTable = "items";      
    public $id;
    public $name;
    public $description;
    public $price;
    public $category_id;   
    public $created; 
	public $modified; 
    private $conn;
	
    public function __construct($db){
        $this->conn = $db;
    }	
	
	function create(){
		
		$stmt = $this->conn->prepare("
			INSERT INTO ".$this->itemsTable."(`name`, `description`, `price`, `category_id`, `created`)
			VALUES(?,?,?,?,?)");
		
		// $this->id = htmlspecialchars(strip_tags($this->id));
		$this->name = htmlspecialchars(strip_tags($this->name));
		$this->description = htmlspecialchars(strip_tags($this->description));
		$this->price = htmlspecialchars(strip_tags($this->price));
		$this->category_id = htmlspecialchars(strip_tags($this->category_id));
		$this->created = htmlspecialchars(strip_tags($this->created));
		
		
		$stmt->bind_param("ssiis", $this->name, $this->description, $this->price, $this->category_id, $this->created);
		
		mysqli_stmt_execute($stmt);

		// return $this;

		if($stmt->execute()){
			return true;
		}
	 
		return false;		 
	}
		
	function read(){	
		if($this->id) {
			$stmt = $this->conn->prepare("SELECT * FROM ".$this->itemsTable." WHERE id = ?");
			$stmt->bind_param("i", $this->id);					
		} else {
			$stmt = $this->conn->prepare("SELECT * FROM ".$this->itemsTable);		
		}		
		$stmt->execute();			
		$result = $stmt->get_result();		
		return $result;	
	}
		
	function readSet($set){	
		$start = (10 * $set) + 1;
		$end = (10 * ($set + 1));

		if($set >= 0) {
			$stmt = $this->conn->prepare("SELECT * FROM ".$this->itemsTable." LIMIT " . $start . "," . $end);
			// $stmt->bind_param("i", $this->id);
		} else {
			$stmt = $this->conn->prepare("SELECT * FROM ".$this->itemsTable);		
		}	
		

		$stmt->execute();
		$result = $stmt->get_result();

		return $result;	
	}

	function update(){
	 
		$stmt = $this->conn->prepare("
			UPDATE ".$this->itemsTable." 
			SET name= ?, description = ?, price = ?, category_id = ?, created = ?
			WHERE id = ?");
	 
		$this->id = htmlspecialchars(strip_tags($this->id));
		$this->name = htmlspecialchars(strip_tags($this->name));
		$this->description = htmlspecialchars(strip_tags($this->description));
		$this->price = htmlspecialchars(strip_tags($this->price));
		$this->category_id = htmlspecialchars(strip_tags($this->category_id));
		$this->created = htmlspecialchars(strip_tags($this->created));
	 
		$stmt->bind_param("ssiisi", $this->name, $this->description, $this->price, $this->category_id, $this->created, $this->id);
		
        //Executing the statement
        mysqli_stmt_execute($stmt);
        //Affected rows
        $count = mysqli_stmt_affected_rows($stmt);
        
        return $count;
		// if($count > 0){
        //     return true;
		// }
	
		// return false;
	}
	
	function delete(){
		
		$stmt = $this->conn->prepare("
			DELETE FROM ".$this->itemsTable." 
			WHERE id = ?");
			
		$this->id = htmlspecialchars(strip_tags($this->id));
	 
		$stmt->bind_param("i", $this->id);
	 
		//Executing the statement
        mysqli_stmt_execute($stmt);
        //Affected rows
        $count = mysqli_stmt_affected_rows($stmt);
        
        return $count;		 
	}
}
?>