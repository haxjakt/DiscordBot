grammar DiscordScript;

IDENTIFIER: [a-zA-Z][a-zA-Z0-9'-']*;
STRING: '"' (~["])* '"';
COMMENT: '#' ~[\r\n]* -> skip;
WS: [ \t\r\n]+ -> skip;

program: scriptDeclaration command*;
//programBody: (command)*;

scriptDeclaration: 'script' IDENTIFIER ';';

command: 'command' commandIdentifiers '{' printString '}';
commandIdentifiers: IDENTIFIER;
printString: 'print' STRING ';';
//genericCommandLine: ~(';')+ ';'; // simplify logic
//commandBody: printString | genericCommandLine;

//eventHandler: 'on-event' IDENTIFIER ('filter' IDENTIFIER)? '{' commandBody '}';

