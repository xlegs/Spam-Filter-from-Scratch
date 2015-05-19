#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>

#define BUFSIZE (4096)

int main(int argc, char *argv[])
{
	--argc,++argv;
	for (;argc;--argc,++argv) {
		char buf[BUFSIZE];
		ssize_t bytes=0;
		int fd=-1;
		char zero = '\0';
		fd = open(argv[0],O_RDONLY);
		if (fd < 0) {
			fprintf(stderr, "could not open %s\n", argv[0]);
			continue;
		}
		do {
			bytes = read(fd, buf, BUFSIZE);
			if (bytes<0) {
				/* read error */
				fprintf(stderr, "read error on file %s\n", argv[0]);
				goto cleanup;
			}
			bytes = write(1, buf, bytes);
		} while (bytes>0);
		if (write(1, &zero, 1)<0) {
			/* write error */
			fprintf(stderr, "write error\n");
		}
	cleanup:
		close(fd);
	}
	return 0;
}
