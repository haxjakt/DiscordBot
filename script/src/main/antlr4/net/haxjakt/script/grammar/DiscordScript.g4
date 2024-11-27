grammar DiscordScript;

// TODO maybe create a separate lexer file for those
IDENTIFIER: [a-zA-Z][a-zA-Z0-9'-']*;
STRING: '"' (~["])* '"';
COMMENT: '#' ~[\r\n]* -> skip;
WS: [ \t\r\n]+ -> skip;

program: scriptDeclaration block*;

scriptDeclaration: 'script' IDENTIFIER ';';

block
    : command
    | messageMenu
    | userMenu
    | buttonHandler
    | modalHandler
    | eventHandler
    ;

command
    : 'command' IDENTIFIER '{' instruction '}';
messageMenu
    : 'message-menu' IDENTIFIER '{' instruction '}';
userMenu
    : 'user-menu' IDENTIFIER '{' instruction '}';
buttonHandler
    : 'button' IDENTIFIER '{' instruction '}';
modalHandler
    : 'modal' IDENTIFIER '{' instruction '}';
eventHandler
    : 'on-event' IDENTIFIER '{' instruction '}';

// TODO instructions shall be generic and programatic (what does that mean xD)
instruction
    : replyDirective
    | descriptionDirective
    | permissionDirective
    | groupDirective
    ;

replyDirective: 'reply' STRING ';';
descriptionDirective: 'description' STRING ';';
permissionDirective: 'permission' IDENTIFIER (',' IDENTIFIER)? ';';
groupDirective: 'group' IDENTIFIER ';';

