grammar DiscordScript;

IDENTIFIER: [a-zA-Z][a-zA-Z0-9'-']*;
STRING: '"' (~["])* '"';
COMMENT: '#' ~[\r\n]* -> skip;
WS: [ \t\r\n]+ -> skip;

program: scriptDeclaration block*;
//programBody: (command)*;

scriptDeclaration: 'script' IDENTIFIER ';';

block:
    command |
    messageMenu;

command: 'command' commandIdentifiers '{' printString '}';
messageMenu: 'message-context' IDENTIFIER '{' printString '}';
commandIdentifiers: IDENTIFIER;
printString: 'print' STRING ';';
//genericCommandLine: ~(';')+ ';'; // simplify logic
//commandBody: printString | genericCommandLine;

//eventHandler: 'on-event' IDENTIFIER ('filter' IDENTIFIER)? '{' commandBody '}';

