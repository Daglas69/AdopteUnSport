/* Checks if the sport to be added does not already exist.
 * Is used to update/refresh the database.
 */
create or replace function insert_sport(n varchar)
returns void
as $proc_insert_sport$
begin
    if not exists (select * from "sports" where lower("name") like lower("n")) then
        insert into "sports" ("name") values ("n");
    end if;
end;
$proc_insert_sport$ language plpgsql;


/* Default */
select insert_sport('Activité non renseignée');

/* A */
select insert_sport('Acrosport');
select insert_sport('Aérobic');
select insert_sport('Airsoft');
select insert_sport('Alpinisme');
select insert_sport('Athlétisme');

/* B */
select insert_sport('Baby-foot');
select insert_sport('Badminton');
select insert_sport('Baseball');
select insert_sport('Basket-ball');
select insert_sport('Biathlon');
select insert_sport('Billard');
select insert_sport('Bowling');
select insert_sport('Boxe');

/* C */
select insert_sport('Canoé');
select insert_sport('Course à pied');
select insert_sport('Course d''orientation');
select insert_sport('Cricket');
select insert_sport('Croquet');
select insert_sport('Crosse');
select insert_sport('Cyclisme');

/* D */
select insert_sport('Danse');
select insert_sport('Duathlon');

/* E */
select insert_sport('Echecs');
select insert_sport('Equitation');
select insert_sport('Escalade');
select insert_sport('Escrime');
select insert_sport('Esports');

/* F */
select insert_sport('Football');

/* G */
select insert_sport('Golf');
select insert_sport('Gymnastique');

/* H */
select insert_sport('Handball');
select insert_sport('Hockey');

/* K */
select insert_sport('Kayak');

/* L */
select insert_sport('Luge');

/* M */
select insert_sport('Musculation');
select insert_sport('Marathon');

/* N */
select insert_sport('Natation');
select insert_sport('Nautisme');

/* P */
select insert_sport('Paintball');
select insert_sport('Parkour');
select insert_sport('Patinage');
select insert_sport('Pétanque');
select insert_sport('Ping-Pong');
select insert_sport('Plongée');

/* Q */
select insert_sport('Quidditch');

/* R */
select insert_sport('Rugby');

/* S */
select insert_sport('Ski');
select insert_sport('Surf');

/* T */
select insert_sport('Tennis');
select insert_sport('Tir à l''arc');
select insert_sport('Triathlon');

/* U */
select insert_sport('Ultimate');

/* V */
select insert_sport('Volley-ball');

/* Y */
select insert_sport('Yoga');
