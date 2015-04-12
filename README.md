# networksim

##Project Summary
Demonstrates functionality	of	some	of	the	OSI	layers	in	the	communications	stack.	In	particular, describes some of	the	key	functions	performed	by	layers	2	(Data	Link),	3	(Network)	and	4 (Transport). The	goal	is	to	emulate	a	simple	scenario	where a	file	is	transferred	form	a	source	node	to	a	destination nodes.

##Project	Details
Each	node should	be represented	by	a	separate	thread,	all	launched	 by	 the	 main	 application (see diagram).	The	MTU	(or	Maximum	Transmit	Unit)	of	all	links	to	be	the	same,	and	equal	to	1400	Bytes. Each	thread	implements	a	limited	set	of	capabilities for	layers	2,	3	and	4.

###Transport
Representings	some	of	the	key functionality	of	layer	4	(transport).	Responsible for breaking	the	message	into	small	segments,	and	construct	the	data	structure	that	will	be	filled	out	by	layers	3	and	4	(the	TCP/IP	Header).	This	layer	will	fill	out the structure and will send	each	segment to layer 3.

###Network
Represents	some	of	the	key functionality	of	layer	3	(routing).	Responsible	for	determining	the	next	hop	for	the	message.The	thread	should	have	an	internal	routing	table	be	pre‐built	from	the	diagram)	and	will	use	that	table	to	lookup	the	net	hop.	That	information	is	then	passed	to	layer 2.

###Data-Link 
Represents	some	of	the	key functionality	of	layer	2	(data	link).	Responsible	for	determining	the	MAC	address	of	the	next	hop,	and	for	encapsulating	the	packet	into	frame	by	adding	a	header	 and	 the	 error	 checking	 code.	 Instead of	coding	ARP	 protocol;	 each	 thread	 has a table	 that	 maps IPs to	MAC	address. This	table is be created ahead of time	by assigning MAC	addresses to	the	specific	interface

##Images
![](https://raw.githubusercontent.com/RushilPatel/networksim/master/img/topology.PNG "Topology")
![](https://raw.githubusercontent.com/RushilPatel/networksim/master/img/img.PNG)
