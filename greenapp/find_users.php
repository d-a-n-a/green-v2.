SELECT a.ID_ANUNT, u.username, ( 6371 * acos( cos( radians(44.446080) ) * cos( radians( l.latitudine) ) 
* cos( radians( l.longitudine ) - radians(26.088237) ) + sin( radians(44.446080) ) * sin(radians(l.latitudine)) ) ) AS distance 
FROM users u, locatii l, anunturi a, categorii c, produse p
WHERE  u.ID_LOCATIE = l.ID_LOCATIE and a.ID_USER = u.ID_USER and a.tip = 'cerere' and p.ID_PRODUS = a.ID_PRODUS and 
c.ID_CATEGORIE = p.ID_CATEGORIE and c.denumire = 'lactate'
HAVING distance < 100
ORDER BY distance 
LIMIT 0 , 20