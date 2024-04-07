create table if not exists printForShow (
    id identity,
    X double not null,
    Y double NOT NULL,
    scale double not null,
    url varchar(50) not null  
);

create table if not exists SanGuoShaCard (
    id identity,
    frame_H double not null,
    frame_W double not null,
    country varchar(5) not null,
    blood int not null,
    max_Blood int not null,
    un_Equal_Blood boolean not null,
    title varchar(50),
    name varchar(50),
    print_Id bigint,
    printer varchar(50),
    copyright varchar(50),
    number varchar(50),
    skill_Ids bigint array
);

create table if not exists Skill (
    id identity,
    header varchar(50) not null,
    description varchar(500) not null
);

create table if not exists YuGiOhCard (
    id identity,
    name varchar(50),
    gradient_Color boolean,
    card_Catalog varchar(20),
    element_Type varchar(20),
    cardtype varchar(20),
    skill_Id bigint,
    print_Id bigint,
    designer varchar(50),
    copyright varchar(50),
    number varchar(50),
    stars int not null,
    atk int not null,
    def int not null
);

create table if not exists CardOrder (
    id identity,
    sanguosha_Card_Ids bigint array,
    yugioh_Card_Ids bigint array,
    name varchar(50),
    province varchar(50),
    city varchar(50),
    state varchar(50),
    detailed_Location varchar(500),
    cc_Number varchar(20),
    Order_Description varchar(500)
);

create table if not exists MyUser (
    id identity,
    username varchar(50) not null,
    password varchar(500) not null,
    fullname varchar(50),
    province varchar(50),
    city varchar(50),
    state varchar(50),
    detailed_Location varchar(500),
    phone_Number varchar(20),
    email varchar(50),
    roles varchar(50) array
);

create table if not exists OrderHistory (
    id identity,
    username varchar(50) not null,
    uncompleted_History_Order_Ids bigint array,
    completed_History_Order_Ids bigint array
);