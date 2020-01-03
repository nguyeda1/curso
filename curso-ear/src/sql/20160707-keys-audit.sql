SELECT k.name,k.number,ka.id,u1.username,ka.created,ka.assignmenttype,u2.username FROM keysassignment ka JOIN keys k ON ka.keys_id=k.id LEFT JOIN sysuser u1 ON ka.auth_id=u1.id LEFT JOIN sysuser u2 ON ka.user_id=u2.id WHERE k.listing_id=5 AND ka.created>NOW()-interval '14 days'
ORDER BY k.id,created ASC;
