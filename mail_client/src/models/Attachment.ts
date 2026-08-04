export class Attachment {
  constructor(
    private _attachmentId: string,
    private _fileName: string,
    private _sizeBytes: number,
    private _mimeType: string
  ) {}
    public get attachmentId(): string {
      return this._attachmentId
    }

    public get fileName(): string{
      return this._fileName
    }
    public get sizeBytes(): number{
      return this._sizeBytes
    }

    public get mimeType(): string{
      return this._mimeType
    }

}
