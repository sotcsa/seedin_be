Table users as U {
    id uuid [pk]
    eth_address varchar
    near_address varchar
    
    "created at" varchar
    Indexes {
        id [pk]
        eth_address 
        near_address 
    }
}
