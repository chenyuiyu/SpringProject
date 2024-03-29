drop table if exists InnerPrint;
drop table if exists SanGuoShaCard;
drop table if exists Skill;
drop table if exists YuGiOhCard;

create table InnerPrint (
    id identity,
    X double not null,
    Y double NOT NULL,
    scale double not null,
    url varchar(50) not null  
);

create table SanGuoShaCard (
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
    skill_Ids int array
);

create table Skill (
    id identity,
    header varchar(50) not null,
    description varchar(500) not null
);

create table YuGiOhCard (
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