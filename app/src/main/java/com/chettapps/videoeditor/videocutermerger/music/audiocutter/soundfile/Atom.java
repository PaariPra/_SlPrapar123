package com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile;

/* loaded from: classes.dex */
class Atom {
    private Atom[] mChildren;
    private byte[] mData;
    private int mFlags;
    private int mSize;
    private int mType;
    private byte mVersion;

    public Atom(String type) {
        this.mSize = 8;
        this.mType = getTypeInt(type);
        this.mData = null;
        this.mChildren = null;
        this.mVersion = (byte) -1;
        this.mFlags = 0;
    }

    public Atom(String type, byte version, int flags) {
        this.mSize = 8;
        this.mType = getTypeInt(type);
        this.mData = null;
        this.mChildren = null;
        this.mVersion = version;
        this.mFlags = flags;
    }

    private void setSize() {
        Atom[] atomArr;
        int size = 8;
        if (this.mVersion >= 0) {
            size = 12;
        }
        if (this.mData != null) {
            size += this.mData.length;
        } else if (this.mChildren != null) {
            for (Atom child : this.mChildren) {
                size += child.getSize();
            }
        }
        this.mSize = size;
    }

    public int getSize() {
        return this.mSize;
    }

    private int getTypeInt(String type_str) {
        return (((byte) type_str.charAt(0)) << 24) | 0 | (((byte) type_str.charAt(1)) << 16) | (((byte) type_str.charAt(2)) << 8) | ((byte) type_str.charAt(3));
    }

    public int getTypeInt() {
        return this.mType;
    }

    public String getTypeStr() {
        return String.valueOf(String.valueOf(String.valueOf("" + ((char) ((byte) ((this.mType >> 24) & 255)))) + ((char) ((byte) ((this.mType >> 16) & 255)))) + ((char) ((byte) ((this.mType >> 8) & 255)))) + ((char) ((byte) (this.mType & 255)));
    }

    public boolean setData(byte[] data) {
        if (this.mChildren != null || data == null) {
            return false;
        }
        this.mData = data;
        setSize();
        return true;
    }

    public byte[] getData() {
        return this.mData;
    }

    public boolean addChild(Atom child) {
        if (this.mData != null || child == null) {
            return false;
        }
        int numChildren = 1;
        if (this.mChildren != null) {
            numChildren = this.mChildren.length + 1;
        }
        Atom[] children = new Atom[numChildren];
        if (this.mChildren != null) {
            System.arraycopy(this.mChildren, 0, children, 0, this.mChildren.length);
        }
        children[numChildren - 1] = child;
        this.mChildren = children;
        setSize();
        return true;
    }

    public Atom getChild(String type) {
        if (this.mChildren == null) {
            return null;
        }
        String[] types = type.split("\\.", 2);
        Atom[] atomArr = this.mChildren;
        for (Atom child : atomArr) {
            if (child.getTypeStr().equals(types[0])) {
                if (types.length != 1) {
                    return child.getChild(types[1]);
                } else {
                    return child;
                }
            }
        }
        return null;
    }

    public byte[] getBytes() {
        Atom[] atomArr;
        byte[] atom_bytes = new byte[this.mSize];
        atom_bytes[0] = (byte) ((this.mSize >> 24) & 255);
        int i2 = 1 + 1;
        atom_bytes[1] = (byte) ((this.mSize >> 16) & 255);
        int i = i2 + 1;
        atom_bytes[i2] = (byte) ((this.mSize >> 8) & 255);
        int i22 = i + 1;
        atom_bytes[i] = (byte) (this.mSize & 255);
        int i3 = i22 + 1;
        atom_bytes[i22] = (byte) ((this.mType >> 24) & 255);
        int i23 = i3 + 1;
        atom_bytes[i3] = (byte) ((this.mType >> 16) & 255);
        int i4 = i23 + 1;
        atom_bytes[i23] = (byte) ((this.mType >> 8) & 255);
        int i24 = i4 + 1;
        atom_bytes[i4] = (byte) (this.mType & 255);
        if (this.mVersion >= 0) {
            int i5 = i24 + 1;
            atom_bytes[i24] = this.mVersion;
            int i25 = i5 + 1;
            atom_bytes[i5] = (byte) ((this.mFlags >> 16) & 255);
            int i6 = i25 + 1;
            atom_bytes[i25] = (byte) ((this.mFlags >> 8) & 255);
            i24 = i6 + 1;
            atom_bytes[i6] = (byte) (this.mFlags & 255);
        }
        if (this.mData != null) {
            System.arraycopy(this.mData, 0, atom_bytes, i24, this.mData.length);
        } else if (this.mChildren != null) {
            for (Atom child : this.mChildren) {
                byte[] child_bytes = child.getBytes();
                System.arraycopy(child_bytes, 0, atom_bytes, i24, child_bytes.length);
                i24 += child_bytes.length;
            }
        }
        return atom_bytes;
    }

    public String toString() {
        String str = "";
        byte[] atom_bytes = getBytes();
        for (int i = 0; i < atom_bytes.length; i++) {
            if (i % 8 == 0 && i > 0) {
                str = String.valueOf(str) + '\n';
            }
            str = String.valueOf(str) + String.format("0x%02X", Byte.valueOf(atom_bytes[i]));
            if (i < atom_bytes.length - 1) {
                str = String.valueOf(str) + ',';
                if (i % 8 < 7) {
                    str = String.valueOf(str) + ' ';
                }
            }
        }
        return String.valueOf(str) + '\n';
    }
}
