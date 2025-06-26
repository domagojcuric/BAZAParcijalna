CREATE DATABASE JavaAdv

USE JavaAdv

CREATE table Polaznik(
		PolaznikID INT IDENTITY (1,1) primary key,
		Ime nvarchar(100),
		Prezime nvarchar(100),
)


CREATE table ProgramObrazovanja(
		ProgramOBrazovanjaID INT IDENTITY (1,1) primary key,
		Naziv nvarchar(100),
		CSVET INT
)

CREATE table Upisi(
		UpisiID INT IDENTITY (1,1) primary key,
		IDProgramOBrazovanja INT,
		IDPolaznik INT,
		foreign key (IDProgramOBrazovanja) REFERENCES ProgramObrazovanja(ProgramOBrazovanjaID),
		foreign key (IDPolaznik) REFERENCES Polaznik(PolaznikID)
)