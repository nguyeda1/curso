SELECT k.number, ka.assignmenttype,ka.created,u.username,ua.username auth
  FROM keysassignment ka JOIN keys k ON ka.keys_id=k.id LEFT JOIN sysuser u ON ka.user_id=u.id LEFT JOIN sysuser ua ON ka.auth_id=ua.id WHERE k.listing_id=9;
