//The code defines a Tag class that stores information about matched substrings and literals in LZ77 compressed data. 
//The Tag class is a data structure to store and manipulate LZ77 compression information.
class Tag {
    public int position, length;
    public char nextSymbol;

    public Tag(int position, int length, char nextSymbol) {
        this.position = position;
        this.length = length;
        this.nextSymbol = nextSymbol;
    }
}