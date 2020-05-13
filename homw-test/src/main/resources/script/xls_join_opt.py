import xlrd as xr;
import xlwt as xw;
from xlutils.copy import copy;

# load excel sheet at read-only mode
rb = xr.open_workbook('../xls_join_opt.xls');
sheet_1 = rb.sheet_by_name('Sheet1');
sheet_2 = rb.sheet_by_name('Sheet2');

# cache data for searching
match_dict = {};
rows_1 = sheet_1.nrows;
for i in range(1, rows_1):
    key = ''.join(sheet_1.row_values(i, start_colx=0, end_colx=3));
    match_dict[key] = i;

# convert read-only workbook to writable
wb = copy(rb);
ws = wb.get_sheet(1);

# search target data
rows_2 = sheet_2.nrows;
for i in range(rows_2):
    if i == 0:
        ws.write(i, 3, 'Refer Value');
        ws.write(i, 4, 'Refer Row');
        continue;
    key = ''.join(sheet_2.row_values(i, start_colx=0, end_colx=3));
    target = 'Mismatch';
    # join opt
    if key in match_dict:
        row = match_dict[key];
        target = sheet_1.cell(row, 3).value;
        if target == '':
            target = 'N/A';
        ws.write(i, 3, str(row + 1));
    ws.write(i, 4, target);
    
# write back excel
wb.save('../xls_join_opt_out.xls');