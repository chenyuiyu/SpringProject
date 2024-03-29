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
    frameH double not null,
    frameW double not null,
    country varchar(5) not null,
    blood int not null,
    maxBlood int not null,
    unEqualBlood boolean not null,
    title varchar(50),
    name varchar(50),
    printId bigint,
    printer varchar(50),
    copyright varchar(50),
    number varchar(50),
    skillIds int array
);

create table Skill (
    id identity,
    header varchar(50) not null,
    description varchar(500) not null
);

create table YuGiOhCard (
    id identity,
    name varchar(50),
    gradientColor boolean,
    cardCatalog varchar(20),
    elementType varchar(20),
    cardtype varchar(20),
    skillId bigint,
    printId bigint,
    designer varchar(50),
    copyright varchar(50),
    number varchar(50),
    stars int not null,
    atk int not null,
    def int not null
);