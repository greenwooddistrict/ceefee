// _cfe: CeeFee support file
//

#include "shlobj.h"
#include "stdio.h"


int main(int argc, char* argv[])
{
	char* op1={"SetFileAttributes"};
	char* op2={"system"};
	char* op3={"load"};


	if ( argc==3 && strcmp(argv[1],op1)==0 ) {
		return SetFileAttributes(argv[2],FILE_ATTRIBUTE_NORMAL);
	} else if ( argc==3 && strcmp(argv[1],op2)==0 ) {
		ShellExecute(NULL,"open",argv[2],NULL,NULL,SW_SHOWNORMAL);
		return 0;
	} else if ( argc==2 && strcmp(argv[1],op3)==0 ) {
		ShellExecute(NULL,"open","ceefee.jar",NULL,NULL,SW_SHOWNORMAL);
		return 0;
	}

	return -1;
}
